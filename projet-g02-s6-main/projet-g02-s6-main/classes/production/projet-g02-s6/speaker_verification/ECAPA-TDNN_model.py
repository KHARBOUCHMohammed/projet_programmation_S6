#!/usr/bin/env python3

try:
    from speechbrain.pretrained import SpeakerRecognition
except ImportError:
    err_msg = "The dependency speechbrain is needed in order to " \
              "use our speaker verification module\n"
    raise ImportError(err_msg)

import torch
import torchaudio
import sys
from os import listdir
from os.path import isfile, join
import os
import speechbrain as sb
from torch import Tensor


class SpeakerVerification:

    def __init__(self, threshold: float):
        self.model = SpeakerRecognition.from_hparams(
            source="speechbrain/spkrec-ecapa-voxceleb",
            savedir="pretrained_models/spkrec-ecapa-voxceleb"
        )

        self.threshold = threshold
        self.source_audio_files = [f for f in listdir("src/speaker_verification/data/source/") if
                                   isfile(join("src/speaker_verification/data/source/", f))]

    def forward(self, file: str) -> bool:
        """
        Check if the user is in the group by using our Deep Learning model.

        We need to rm the wav files because speechbrains creates copy of the file
        each time we run a forward.

        :param file: location of the audio file
        :return:
        """
        location_file = os.getcwd() + "/src/speaker_verification/data/test/" + file
        check_wav_file = audio_pipeline(location_file)

        for source_file in self.source_audio_files:

            location_source_file = os.getcwd() + "/src/speaker_verification/data/source/" + source_file

            source_wav_file = audio_pipeline(location_source_file)

            score, prediction = self.model.verify_batch(
                source_wav_file,
                check_wav_file
            )
            if score >= self.threshold and prediction:
                return True

        return False


def audio_pipeline(wav: str) -> Tensor:
    """
    :param wav: path file to wav signal
    :return: Tensor
    """
    info = torchaudio.info(wav)
    sig = sb.dataio.dataio.read_audio(wav)

    if info.num_channels > 1:
        sig = torch.mean(sig, dim=1)

    resampled = torchaudio.transforms.Resample(
        info.sample_rate, 16_000,
    )(sig)

    return resampled


def main() -> bool:
    assert isinstance(sys.argv[1], str)
    assert isinstance(sys.argv[2], str)
    threshold = float(sys.argv[2])
    if 1 >= threshold >= 0.01:
        if SpeakerVerification(threshold=float(sys.argv[2])).forward(sys.argv[1]):
            return True
        else:
            return False
    else:
        print("Error threshold is superior to 1 or inferior to 0.01")
        return False


if __name__ == "__main__":
    print(main())

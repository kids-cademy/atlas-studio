package com.kidscademy.atlas.studio.tool;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WaveStream implements Closeable {
    private final AudioInputStream stream;
    private final byte[] frame;
    private final int samplesCount;
    private final Sampler sampler;

    public WaveStream(File file) throws IOException {
	// getAudioInputStream returns am audio reader decorator but does not provide
	// buffering; need to buffer input stream explicitly otherwise reading speed
	// degrades with order of magnitude
	try {
	    this.stream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
	} catch (UnsupportedAudioFileException e) {
	    throw new IOException(e);
	}

	AudioFormat format = stream.getFormat();
	this.frame = new byte[format.getFrameSize()];
	this.samplesCount = (int)stream.getFrameLength();

	this.sampler = SamplerFactory.getSampler(format);
	if (this.sampler == null) {
	    throw new IllegalStateException("Unsupported format: " + format);
	}
    }

    public int getSamplesCount() {
	return samplesCount;
    }

    public Double readSample() throws IOException {
	int offset = 0;
	int length = frame.length;
	int n = 0;

	while ((n = stream.read(frame, offset, length)) != frame.length) {
	    if (n == -1) {
		return null;
	    }
	    offset += n;
	    length -= n;
	}

	return sampler.nextSample(frame);
    }

    @Override
    public void close() throws IOException {
	stream.close();
    }

    // --------------------------------------------------------------------------------------------

    private static interface Sampler {
	double nextSample(byte[] frame);
    }

    private static class SamplerFactory {
	public static Sampler getSampler(AudioFormat format) {
	    AudioFormat.Encoding encoding = format.getEncoding();
	    boolean bigEndien = format.isBigEndian();
	    int channels = format.getChannels();
	    int sampleSize = format.getSampleSizeInBits();

	    if (encoding == AudioFormat.Encoding.PCM_UNSIGNED) {
		if (channels == 1) {
		    if (sampleSize == 8) {
			return new UnsignedPcmMono8Sampler();
		    }
		    if (sampleSize == 16) {
			if (bigEndien) {
			    return new UnsignedPcmMono16BigSampler();
			}
			return new UnsignedPcmMono16LittleSampler();
		    }
		    return null;
		}

		if (channels == 2) {
		    if (sampleSize == 8) {
			return new UnsignedPcmStereo8Sampler();
		    }

		    if (sampleSize == 16) {
			if (bigEndien) {
			    return new UnsignedPcmStereo16BigSampler();
			}
			return new UnsignedPcmStereo16LittleSampler();
		    }
		    return null;
		}

		return null;
	    }

	    if (encoding == AudioFormat.Encoding.PCM_SIGNED) {
		if (channels == 1) {
		    if (sampleSize == 8) {
			return new SignedPcmMono8Sampler();
		    }

		    if (sampleSize == 16) {
			if (bigEndien) {
			    return new SignedPcmMono16BigSampler();
			}
			return new SignedPcmMono16LittleSampler();
		    }

		    return null;
		}

		if (channels == 2) {
		    if (sampleSize == 8) {
			return new SignedPcmStereo8Sampler();
		    } else if (sampleSize == 16) {
			if (bigEndien) {
			    return new SignedPcmStereo16BigSampler();
			}
			return new SignedPcmStereo16LittleSampler();
		    }
		    return null;
		}

		return null;
	    }

	    return null;
	}
    }

    private static class UnsignedPcmMono8Sampler implements Sampler {
	@Override
	public double nextSample(byte[] frame) {
	    assert frame.length == 1;
	    return ((int) frame[0] & 0xFF) / 256.0;
	}
    }

    private static class UnsignedPcmStereo8Sampler implements Sampler {
	@Override
	public double nextSample(byte[] frame) {
	    assert frame.length == 2;
	    return (((int) frame[0] & 0xFF) + ((int) frame[1] & 0xFF)) / 512.0;
	}
    }

    private static class UnsignedPcmMono16BigSampler implements Sampler {
	@Override
	public double nextSample(byte[] frame) {
	    assert frame.length == 2;
	    return (int) (frame[0] << 8 | (frame[1] & 0xFF)) / 65536.0;
	}
    }

    private static class UnsignedPcmStereo16BigSampler implements Sampler {
	@Override
	public double nextSample(byte[] frame) {
	    assert frame.length == 4;
	    int left = frame[0] << 8 | (frame[1] & 0xFF);
	    int right = frame[2] << 8 | (frame[3] & 0xFF);
	    return (left + right) / 131072.0;
	}
    }

    private static class UnsignedPcmMono16LittleSampler implements Sampler {
	@Override
	public double nextSample(byte[] frame) {
	    assert frame.length == 2;
	    return (int) (frame[1] << 8 | (frame[0] & 0xFF)) / 65536.0;
	}
    }

    private static class UnsignedPcmStereo16LittleSampler implements Sampler {
	@Override
	public double nextSample(byte[] frame) {
	    assert frame.length == 4;
	    int left = frame[1] << 8 | (frame[0] & 0xFF);
	    int right = frame[3] << 8 | (frame[2] & 0xFF);
	    return (left + right) / 131072.0;
	}
    }

    private static class SignedPcmMono8Sampler implements Sampler {
	@Override
	public double nextSample(byte[] frame) {
	    assert frame.length == 1;
	    return (frame[0] + 127) / 256.0;
	}
    }

    private static class SignedPcmStereo8Sampler implements Sampler {
	@Override
	public double nextSample(byte[] frame) {
	    assert frame.length == 2;
	    return ((int) (frame[0] + frame[1]) + 256) / 512.0;
	}
    }

    private static class SignedPcmMono16BigSampler implements Sampler {
	@Override
	public double nextSample(byte[] frame) {
	    assert frame.length == 2;
	    return ((int) (frame[0] << 8 | (frame[1] & 0xFF)) + 32767) / 65536.0;
	}
    }

    private static class SignedPcmStereo16BigSampler implements Sampler {
	@Override
	public double nextSample(byte[] frame) {
	    assert frame.length == 4;
	    int left = frame[0] << 8 | (frame[1] & 0xFF);
	    int right = frame[2] << 8 | (frame[3] & 0xFF);
	    return (left + right + 65534) / 131072.0;
	}
    }

    private static class SignedPcmMono16LittleSampler implements Sampler {
	@Override
	public double nextSample(byte[] frame) {
	    assert frame.length == 2;
	    return ((int) (frame[1] << 8 | (frame[0] & 0xFF)) + 32767) / 65536.0;
	}
    }

    private static class SignedPcmStereo16LittleSampler implements Sampler {
	@Override
	public double nextSample(byte[] frame) {
	    assert frame.length == 4;
	    int left = frame[1] << 8 | (frame[0] & 0xFF);
	    int right = frame[3] << 8 | (frame[2] & 0xFF);
	    return (left + right + 65534) / 131072.0;
	}
    }
}

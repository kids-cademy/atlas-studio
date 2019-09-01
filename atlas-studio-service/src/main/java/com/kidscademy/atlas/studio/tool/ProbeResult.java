package com.kidscademy.atlas.studio.tool;

import java.util.Map;

public class ProbeResult {
    Format format;
    Stream[] streams;

    public ProbeResult() {
    }

    /**
     * Test constructor.
     * 
     * @param duration
     */
    public ProbeResult(double duration) {
	Stream stream = new Stream();
	streams = new Stream[] { stream };
	stream.duration = duration;
    }

    /**
     * Test constructor.
     * 
     * @param filename
     * @param size
     * @param codec_long_name
     * @param duration
     * @param channels
     * @param sample_rate
     * @param bit_rate
     */
    public ProbeResult(String filename, int size, String codec_long_name, double duration, int channels,
	    int sample_rate, int bit_rate) {
	format = new Format();
	format.filename = filename;
	format.size = size;

	Stream stream = new Stream();
	streams = new Stream[] { stream };
	stream.codec_long_name = codec_long_name;
	stream.duration = duration;
	stream.channels = channels;
	stream.sample_rate = sample_rate;
	stream.bit_rate = bit_rate;
    }

    static class Format {
	String filename;
	int nb_streams;
	int nb_programs;
	String format_name;
	String format_long_name;
	double start_time;
	double duration;
	int size;
	int bit_rate;
	int probe_score;
    }

    static class Stream {
	int index;
	String codec_name;
	String codec_long_name;
	String codec_type;
	String codec_time_base;
	String codec_tag_string;
	String codec_tag;
	String sample_fmt;
	int sample_rate;
	int channels;
	String channel_layout;
	int bits_per_sample;
	String r_frame_rate;
	String avg_frame_rate;
	String time_base;
	double start_pts;
	double start_time;
	long duration_ts;
	double duration;
	int bit_rate;
	Map<String, Integer> disposition;
    }
}

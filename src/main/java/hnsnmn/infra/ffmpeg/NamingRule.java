package hnsnmn.infra.ffmpeg;

import hnsnmn.domain.job.OutputFormat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 18.
 * Time: 오후 9:59
 * To change this template use File | Settings | File Templates.
 */
public interface NamingRule {
	String createName(OutputFormat format);

	public static final NamingRule DEFAULT = new DefaultNamingRule();

	public static class DefaultNamingRule implements NamingRule {
		private Random random = new Random();
		private String baseDir;

		public DefaultNamingRule() {
			baseDir = System.getProperty("java.io.tmpdir");
		}

		public void setBaseDir(String baseDir) {
			this.baseDir = baseDir;
		}

		@Override
		public String createName(OutputFormat format) {
			String fileName = getFileNameFromTime();
			File file = createFileFromFileNameAndFormat(format, baseDir, fileName);
			return file.getPath();
		}

		private String getFileNameFromTime() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = dateFormat.format(new Date());
			int num = random.nextInt(1000);
			String fileName = time + "_" + num;
			return fileName;
		}

		private File createFileFromFileNameAndFormat(OutputFormat format, String tempDir, String fileName) {
			File file = new File(tempDir, fileName + "." + format.getFileExtension());
			return file;
		}
	}
}

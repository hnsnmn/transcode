import org.junit.Test;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 11.
 * Time: 오후 5:52
 * To change this template use File | Settings | File Templates.
 */
public class TranscodingServiceTest {

	@Test
	public void transcodeSuccessfully() {
		// 미디어 원본으로부터 파일을 로컬에 복사한다.
		Long jobId = new Long(1);
		File multimediaFile = copyMultimediaSourceToLocal(jobId);
		// 로컬에 복사된 파일을 변환처리한다.
		// 로컬에 복사된 파일로부터 이미지를 추출한다.
		// 변환된 결과 파일과 썸네일 이미지를 목적지에 저장한다.
		// 결과를 통지한다.
	}

	private File copyMultimediaSourceToLocal(Long jobId) {
		return null;
	}

}

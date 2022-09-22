package com.bithumbsystems.lrc.management.api.core.util;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.file.exception.FileException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileUtil {

  public static final String[] ALLOW_FILE_EXT_DEFAULT = {"AI", "HWP", "MOV", "MP4", "AVI", "MKV", "TXT", "DOC", "DOCX", "XLS", "XLSX", "PPT", "PPTX", "PDF", "JPG", "JPEG", "PNG", "GIF"};
  public static final long ALLOW_FILE_MAX_SIZE_DEFAULT = 100;

  public static String readResourceFile(String path) throws IOException {
    ClassPathResource resources = new ClassPathResource(path);
    try (InputStream inputStream = resources.getInputStream()) {
      String string = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      System.out.println(string);
      return string;
    }
  }


  /**
   * 첨부 파일 사이즈 체크
   * @param maxSize
   * @param fileSize
   * @return
   * @throws FileException
   */
  public static String getFileSize(long maxSize, long fileSize) throws FileException {
    double fileSizeInKB = (double)fileSize/1024;
    fileSizeInKB = Math.round(fileSizeInKB * 10) / 10.0;

    double fileSizeInMB = fileSizeInKB/1024;
    fileSizeInMB = Math.round(fileSizeInMB * 10) / 10.0;

    //100 메가 이상 업로드 금지.
    if(fileSizeInMB > maxSize){
      throw new FileException(ErrorCode.INVALID_MAX_FILE_SIZE);
    }
    String fileSizeStr = (fileSizeInMB > 1)? fileSizeInMB + "MB" : fileSizeInKB + "KB";
    return fileSizeStr;
  }

  /**
   * 첨부파일 확장자 체크
   * @param fileName
   * @return
   * @throws FileException
   */
  public static String getFileExt(String[] allowExt, String fileName) throws FileException {
    String ext = "";
    try {
      ext = StringUtils.getFilenameExtension(fileName).toUpperCase();
    }catch(Exception e){
      throw new FileException(ErrorCode.INVALID_FILE_EXT);
    }
    if(Arrays.asList(allowExt).contains(ext) == false){
      throw new FileException(ErrorCode.INVALID_FILE_EXT);
    }
    return ext;
  }

}

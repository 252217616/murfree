package com.qianbao.print.common.utils;

import com.google.common.base.Splitter;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 * @author Shaojun Liu <liushaojun@qianbao.com>
 * @create 2018/4/8
 */
public interface Strs {

  char C_SPACE = ' ';
  char C_TAB = '	';
  char C_DOT = '.';
  char C_SLASH = '/';
  char C_BACKSLASH = '\\';
  char C_CR = '\r';
  char C_LF = '\n';
  char C_UNDERLINE = '_';
  char C_COMMA = ',';
  char C_DELIM_START = '{';
  char C_DELIM_END = '}';
  char C_COLON = ':';

  String SPACE = " ";
  String TAB = "	";
  String DOT = ".";
  String DOUBLE_DOT = "..";
  String SLASH = "/";
  String BACKSLASH = "\\";
  String EMPTY = "";
  String CR = "\r";
  String LF = "\n";
  String CRLF = "\r\n";
  String UNDERLINE = "_";
  String COMMA = ",";
  String DELIM_START = "{";
  String DELIM_END = "}";
  String COLON = ":";

  String HTML_NBSP = "&nbsp;";
  String HTML_AMP = "&amp";
  String HTML_QUOTE = "&quot;";
  String HTML_LT = "&lt;";
  String HTML_GT = "&gt;";

  String EMPTY_JSON = "{}";

  /**
   * 格式化模板字符串
   * ```
   * formt("This is a {} string.","template");
   *
   * ```
   *
   * @param str 模板字符串
   * @param args 参数
   * @return 格式化的字符串
   */
  static String format(String str, Object... args) {
    if (StringUtils.isEmpty(str)) {
      return str;
    }
    if(args.length == 0){
      return str.replaceAll("\\{}", String.valueOf(""));
    }
    for (Object arg : args) {
      str = str.replaceFirst("\\{}", String.valueOf(arg));
    }

    return str;
  }

  static List<String> split(String str, String regex){
    if (str == null)
      return ListUtil.emptyList();

    return Splitter.on(Pattern.compile(regex))
        .trimResults()
        .omitEmptyStrings()
        .splitToList(str);
  }

  static List<String> split(String str) {
    if(str == null) return ListUtil.emptyList();
    return Splitter.on(Pattern.compile("[,;；，]"))
        .omitEmptyStrings()
        .splitToList(str);
  }

}

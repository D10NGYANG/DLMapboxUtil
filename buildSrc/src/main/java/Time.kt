package utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 时间格式化
 * @param pattern String
 * @return (String..String?)
 */
fun timeFormat(pattern: String = "yyyyMMddHHmmss") = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())
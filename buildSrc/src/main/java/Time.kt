import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 时间格式化
 * @param pattern String
 * @return String
 */
fun timeFormat(pattern: String = "yyyyMMddHHmmss"): String = DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now())
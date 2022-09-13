package org.example.web.dto;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.regex.Matcher;

public class BookRegexToRemove {
    private final String regex = "(id|size|author|title)\\s*(>=|<=|!=|=|>|<)+\\s*(.+)";
    @Pattern(regexp = regex, message = "Regex format must be:\n[id,size,author,title] [=,!=,>,<,>=,<,=>] <text>")
    private final String text;
    @NotEmpty(message = "Wrong type")
    private String type;
    @NotEmpty(message = "Wrong operator")
    private String operator;
    @NotEmpty(message = "Wrong value")
    private String value;
    @AssertFalse(message = "Value for type [id,size] must be > 0 integer")
    private boolean exceptionInt;
    @AssertFalse(message = "Operator for type [author,title] must be [=,!=]")
    private boolean exceptionOperator;

    public BookRegexToRemove(@Pattern(regexp = regex) String text) {
        this.text = text;
        parseText();
    }

    private void parseText() {
        java.util.regex.Pattern re = java.util.regex.Pattern.compile(regex, java.util.regex.Pattern.CASE_INSENSITIVE);
        Matcher matcher = re.matcher(text);
        if (matcher.find()) {
            type = matcher.group(1).toLowerCase();
            operator = matcher.group(2);
            value = matcher.group(3).trim();
            // проверка для id size что значение int
            if (Arrays.asList("id", "size").contains(type)) {
                Integer valueInt = null;
                try {
                    valueInt = Integer.parseInt(matcher.group(3).trim());  // проверко integer ли значение
                } catch (Exception ex) {
                    exceptionInt = true;
                }
                if (valueInt == null || valueInt < 0) {
                    exceptionInt = true;
                }
            } else {
                if (!Arrays.asList("=", "!=").contains(operator)) {
                    exceptionOperator = true;
                }
            }
        } else {
            type = "null";
            operator = "null";
            value = "null";
        }
    }

    public String getType() {
        return type;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }
}

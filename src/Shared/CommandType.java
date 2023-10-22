package Shared;

import java.io.Serializable;

public enum CommandType implements Serializable {
    HELP,
    INFO,
    SHOW,
    CLEAR,
    REMOVE_KEY,
    REPLACE_IF_GREATER,
    REPLACE_IF_LOWER,
    REMOVE_GREATER_KEY,
    COUNT_LESS_THAN_OSCARS_COUNT,
    PRINT_ASCENDING,
    INSERT,
    UPDATE,
    EXECUTE_SCRIPT,
    PRINT_FIELD_DESCENDING_OSCARS_COUNT
}

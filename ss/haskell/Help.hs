module Help where

--Simple function, given string (of command name), return help string
help::String->String
help "load" = "Load a CSV file. Args = filename"
help "save" = "Save a CSV file. Args = filename"
help "report" = "Run built in report. Args = registrations | completions"
help "count" = "Count records that meet conditions. Args = conditions"
help "list" = "List records that meet conditions. Args = conditions"
help "distinct" = "Count number of unique values in a column. Args = column"
help "output" = "Output all queries to a file. Args = filename"
help "nooutput" = "Output all queries to standard output"
help "date-fix" = "Rewrite date column to a particular format. Args = date-format"
help "grid-fix" = "Rewrite coordinate column to a particular format. Args = grid-format"
help "reformat" = "Reformat a column. Args = uppercase | capitalize | lowercase | trim"
help "sort" = "Sort the spreadsheet. Args = (field name, ascending | descending)"
help "select" = "Select part of the spreadsheet. Args = conditions | all"
help "show" = "Show selected rows"
help "update" = "Update a field. Args = row col data"
help "delete" = "Delete a row. Args = row"
help "insert" = "Insert a new row. Args = row (col=data)"
help "help" = "Hello!"
help "quit" = "Exit the program"
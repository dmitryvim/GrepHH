# Java grep simple by mhty

This is grep function.

To use it like lib, take

    Grep.build(String substring_to_search,  
                String[] array_of_filenames_where_to_search, 
                FilterOutputStream for_example_System_dot_out)
                    .searchAllFiles(); 

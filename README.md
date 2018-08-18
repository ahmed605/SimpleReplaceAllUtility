# SimpleReplaceAllUtility

This utility enables you to replace all of 1 occurence of a string to another based on a csv file, in all files of a directory. 

Example: CSV File 

| OriginalText        | RealText           | 
| ------------- |:-------------:|
| field_104025_t	| threadLock |

And it will scan a directory of your choosing and replace all files with field_104025_t in any part of the file
and replace it with threadLock. 

Just run this jar with 2 arguments, the /path/to/csvFile.csv and /path/to/directory/to/scan

If your csv has multiple rows it will replace all files with all occurences of the fields with the realtext value. 

This program will always skip the first header row. 

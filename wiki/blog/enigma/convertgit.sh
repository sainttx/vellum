
sed 's/>\!/>/g' |
  sed 's/<tt>\!//g' | 
  sed 's/<tt>//g' | 
  sed 's/<\/tt>//g' | 
  sed 's/\!G/G/g' | 
  sed 's/{{{/<pre>/g' | 
  sed 's/}}}/<\/pre>/g' | 
  sed 's/```java/<pre>/g' | 
  sed 's/```shell/<pre>/g' |
  sed 's/```/<\/pre>/g' |
  sed 's/<\/pre>/\n*/g' 

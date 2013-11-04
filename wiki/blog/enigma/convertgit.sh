
cd 

set -u

name="$1"

c0sed() {
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
  sed ':a;N;$!ba;s/<\/pre>\n*/<\/pre>/g'
}

cat NetBeansProjects/git/vellum.wiki/$name.md | c0sed > NetBeansProjects/svn/vellum/wiki/blog/enigma/$name.html


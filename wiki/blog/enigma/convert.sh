
cd 

set -u 

name=$1
pattern="$2"

[ -f NetBeansProjects/svn/vellum/wiki/$name.wiki ] || exit 1

cp NetBeansProjects/svn/vellum/wiki/$name.wiki ~/tmp/.

cd NetBeansProjects/svn/vellum/wiki/blog/enigma/

[ -f $name.html ] && cp $name.html ~/tmp/.

cat ~/tmp/$name.wiki | grep "$pattern" -A9999 > $name.content.html

sed -i 's/>\!/>/g' $name.content.html 
sed -i 's/<tt>\!//g' $name.content.html 
sed -i 's/<tt>//g' $name.content.html 
sed -i 's/<\/tt>//g' $name.content.html 
sed -i 's/\!G/G/g' $name.content.html 
sed -i 's/{{{/<pre>/g' $name.content.html 
sed -i 's/}}}/<\/pre>/g' $name.content.html 

cat $name.head.html > $name.html
cat $name.content.html | sed ':a;N;$!ba;s/<\/pre>\n*/<\/pre>/g' >> $name.html



cd 

cp NetBeansProjects/svn/vellum/wiki/DualControl.wiki ~/tmp/.

cd NetBeansProjects/svn/vellum/wiki/blog/enigma/

cp DualControl.html ~/tmp/.

cat ~/tmp/DualControl.wiki | grep '<h4>PCI' -A9999 > DualControl.content.html 

sed -i 's/<tt>\!//g' DualControl.content.html 
sed -i 's/<tt>//g' DualControl.content.html 
sed -i 's/<\/tt>//g' DualControl.content.html 
sed -i 's/{{{/<pre>/g' DualControl.content.html 
sed -i 's/}}}/<\/pre>/g' DualControl.content.html 

cat DualControl.head.html DualControl.content.html > DualControl.html


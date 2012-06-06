recipients=`cat /scripts/recipients`
recipients_gmail=`cat /scripts/recipients_gmail`

tmp=/tmp/`basename $0`-$USER-$$.tmp

tee=0
pre=0
hostname=`hostname -s`
from=$USER@`hostname`
subject="$hostname mailer"
timestamp=`date +'%Y-%m-%d %T'`

while [ $# -gt 0 ]
do
  if [ "$1" = "--pre" ]
  then
    pre=1
  elif [ "$1" = "--tee" ]
  then
    tee=1
  elif [ "$1" = "gmail" ]
  then
    recipients=$recipients_gmail
  elif [ "$1" = "-s" ]
  then
    if [ $# -gt 0 ]
    then
      shift
      subject=$1
    fi
  else
    echo "$1" | grep -q "@"
    if [ $? -eq 0 ]
    then
      break
    else
      subject=$1
    fi
  fi
  shift
done

if [ $# -gt 0 ]
then
  recipients=$@
fi

cat > $tmp

if [ `cat $tmp | wc -c | bc` -le 1 ]
then
  rm -f $tmp
  exit 1
fi

if [ `cat $tmp | grep -v "^<pre>\|^Subject: \|^To: \|^From: " | wc -l | bc` -eq 0 ]
then
  rm -f $tmp
  exit 1
fi

if [ $pre -eq 0 ]
then
  cat $tmp | grep -q "<"
  if [ $? -ne 0 ]
  then
    pre=1
  fi
fi

if [ $pre -eq 1 ]
then
  cat $tmp | sed -e 's/&/\&amp;/g' | sed -e 's/</\&lt;/g' | sed -e 's/>/\&gt;/g' >> $tmp.tmp
  mv -f $tmp.tmp $tmp
  if cat $tmp | grep -q "^WARN\|^ERROR"
  then
    echo "<pre>" > $tmp.tmp
    cat $tmp | grep "^WARN\|^ERROR" >> $tmp.tmp
    echo "<hr>" >> $tmp.tmp
    cat $tmp | grep -v "^<pre>" >> $tmp.tmp
    mv -f $tmp.tmp $tmp
  else 
    echo "<pre>" > $tmp.tmp
    cat $tmp | grep -v "^<pre>" >> $tmp.tmp
    mv -f $tmp.tmp $tmp
  fi
fi

cat $tmp | grep -q "^Subject: "
if [ $? -eq 0 ]
then
  subject=`cat $tmp | grep "^Subject: " | cut -b10-99 | head -1`
fi

cat $tmp | grep -v "^Subject: \|^To: \|^From: " | mutt -e 'set content_type="text/html"' -s "$subject" $recipients

if [ $tee -eq 1 ]
then
  echo "from: $from"
  echo "to: $recipients"
  echo "date: $timestamp"
  echo "subject: $subject"
  echo "--"
  cat $tmp
fi

rm -f $tmp

exit 0

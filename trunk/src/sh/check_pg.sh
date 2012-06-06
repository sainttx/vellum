
serviceName=PostgreSQL
port=5432

query_slow="
  select age(now(), query_start), usename, procpid, substr(current_query, 1, 64)
  from pg_stat_activity
  where age(now(), query_start) > interval '10 seconds'
  and current_query <> '<IDLE>'
  order by query_start
"

query_slowest_seconds="
  select extract(epoch from age(now(), query_start))::integer as seconds
  from pg_stat_activity
  where age(now(), query_start) > interval '10 seconds'
  and current_query <> '<IDLE>'
  order by age(now(), query_start) desc
  limit 1
"

count=`psql -p $port -t -c "$query_slow" | grep -v "IDLE\|COPY\|autovacuum" | grep "|" | wc -l`
if [ $count -gt 0 ]
then
  slowestSeconds=`psql -p $port -t -c "$query_slowest_seconds" | bc`
  if [ -n "$slowestSeconds" -a $slowestSeconds -gt 10 ]
  then
    message="slowest $slowestSeconds seconds, loadavg `cat /proc/loadavg`"
    statusName=WARNING
    statusCode=1
    if [ $count -gt 10 -a $slowestSeconds -gt 180 ]
    then
      statusName=CRITICAL
      statusCode=2
    fi
    host=`hostname -s`
    status="$serviceName $statusName - $host, $message"
    echo "Subject: $serviceName $statusName - $host"
    echo "<b>$status</b>"
    echo "<pre>"
    psql -p $port -c "$query_slow"
    exit $statusCode
  fi
fi

exit 0
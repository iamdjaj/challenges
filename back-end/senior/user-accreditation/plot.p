set terminal png size 1000,1000
set size 1, 1
set output "benchmkark.png"
set title "Response Time"
set key left top
set grid y
set xdata time
set timefmt "%s"
set format x "%S"
set xlabel 'seconds'
set ylabel "response time (ms)"
set datafile separator '\t'
plot "out.data" every ::2 using 2:5 title 'response time' with points
exit

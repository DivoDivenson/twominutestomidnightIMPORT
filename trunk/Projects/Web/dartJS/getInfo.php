<?php

$station = $_REQUEST["station"];
$urlStr = "http://www.irishrail.ie/realtime/publicinfo.asp?strLocation=".$station;
exec('curl '.$urlStr, $output);
print_r($output[0]);
/*echo '<?xml version="1.0"?><rss version="2.0"><channel><title>irishrail.ie: Tara Street -Current time is: 11:26am</title><description>Estimated Time of Arrival:</description><link>http://www.irishrail.ie</link><item><title>11:30  to Greystones</title><description>E110 - Origin : Howth - Destination : Greystones | -   Expected departure at Tara Street :11:30 | -   Scheduled departure at 11:28</description></item><item><title>11:33  to Dublin Pearse</title><description>P609 - Origin : Balbriggan - Destination : Dublin Pearse | -   Expected departure at Tara Street :11:33 | -   Scheduled departure at 11:33</description></item><item><title>11:37  to Howth</title><description>E912 - Origin : Bray - Destination : Howth | -   Expected departure at Tara Street :11:37 | -   Scheduled departure at 11:36</description></item><item><title>11:43  to Bray</title><description>E211 - Origin : Howth - Destination : Bray | -   Expected departure at Tara Street :11:43 | -   Scheduled departure at 11:43</description></item><item><title>11:51  to Malahide</title><description>E811 - Origin : Greystones - Destination : Malahide | -   Expected departure at Tara Street :11:51 | -   Scheduled departure at 11:51</description></item><item><title>11:58  to Greystones</title><description>E111 - Origin : Howth - Destination : Greystones | -   Expected departure at Tara Street :11:58 | -   Scheduled departure at 11:58</description></item><item><title>12:06  to Howth</title><description>E913 - Origin : Bray - Destination : Howth | -   Expected departure at Tara Street :12:06 | -   Scheduled departure at 12:06</description></item><item><title>12:07  to Arklow</title><description>A604 - Origin : Dublin Connolly - Destination : Arklow | -   Expected departure at Tara Street :12:07 | -   Scheduled departure at 12:07</description></item><item><title>12:13  to Maynooth</title><description>D912 - Origin : Dublin Pearse - Destination : Maynooth | -   Expected departure at Tara Street :12:13 | -   Scheduled departure at 12:13</description></item><item><title>12:13  to Bray</title><description>E212 - Origin : Howth - Destination : Bray | -   Expected departure at Tara Street :12:13 | -   Scheduled departure at 12:13</description></item><item><title>12:21  to Howth</title><description>E945 - Origin : Greystones - Destination : Howth | -   Expected departure at Tara Street :12:21 | -   Scheduled departure at 12:21</description></item><item><title>12:21  to Dublin Pearse</title><description>P657 - Origin : Maynooth - Destination : Dublin Pearse | -   Expected departure at Tara Street :12:21 | -   Scheduled departure at 12:21</description></item></channel></rss>';*/



?>
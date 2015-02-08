<?php

$message = 'iTE,356496041251380,$GPRMC,122751.000,A,0410.4703,N,07330.6449,E,0.00,108.92,211214,,,A*66,ST,N8257,M31,4070mV,34,1.7,472,2,1F40,5DD6,46,472,2,1F40,5DDA,54,472,2,1F40,0000,59';
// $message = '$MGV002,860719020193193,DeviceName,R,240214,104742,A,2238.20471,N,11401.97967,E,00,03,00,1.20,0.462,356.23,137.9,1.5,460,07,262C,0F54,25,0000,0000,0,0,0,28.5,28.3,,10,100,Timer;!';

// $sock = fsockopen('localhost', 1234);
$sock = fsockopen('62.113.202.118', 1234);
fwrite($sock, $message);

$response = fgets($sock);
echo $response, PHP_EOL;

fclose($sock);

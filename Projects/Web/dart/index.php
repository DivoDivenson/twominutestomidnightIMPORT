<html>
	<head>
		<title>DART Times</title>
		<meta name="viewport" content="user-scalable=no, width=device-width" />
		<link rel="stylesheet" type="text/css" href="android.css" media="only screen and (max-width: 480px)" /> 
		<!--  <link rel="stylesheet" type="text/css" href="desktop.css" media="screen and (min-width: 481px)" /> -->
	</head>
	<body>
		<div id="header">
			<h1><a href="./">Dart Times</a></h1>
			<?php
				$places=array("Sutton","Pearse");
				$all_places=array("Bayside", "Blackrock", "Bray", "Clontarf", "Connolly", "Dalkey", "Dun_Laoghaire", "Grand_Canal_Dock", "Glenageary", "Greystones", "Harmonstown", "Howth", "Howth_Jct", "Kilbarrack", "Killester", "Killiney", "Lansdowne_Road", "Malahide", "Pearse", "Portmarnock", "Raheny", "Salthill", "Sandycove", "Sandymount", "Seapoint", "Shankill", "Sutton", "Sydney_Parade", "Tara_Street");

				echo '<ul>';
				foreach($all_places as $value)
				{
					$tmp = '<li><a href="./time.php?place='.$value.'">'.str_replace("_", " ",$value).'</a></li>';
					echo $tmp;
				}
				echo '</ul>';
			?>

 		</div>
	</body>


</html>

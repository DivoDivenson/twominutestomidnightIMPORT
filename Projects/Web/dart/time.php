<html>
	<head>
		<?php echo '<title>'.$_GET["place"].' times</title>'; ?>
		<meta name="viewport" content="user-scalable=no, width=device-width" />
		<link rel="stylesheet" type="text/css" href="android.css" media="only screen and (max-width: 480px)" /> 
		<link rel="stylesheet" type="text/css" href="desktop.css" media="screen and (min-width: 481px)" />
	</head>
	<body>
		<div id="header">
		<?php echo '<h1><a href="./">'.str_replace("_", " ", $_GET["place"])." times</a></h1>\r"; ?>
			<h2>
			<?php

				$url_places=array("Bayside"=>"BYSDE", "Blackrock"=>"BROCK","Clontarf"=>"CTARF", "Dalkey"=>"DLKEY", "Dun_Laoghaire"=>"DLERY","Grand_Canal_Dock"=>"GCDK", "Greystones"=>"GSTNS", "Harmonstown"=>"HTOWN","Kilbarrack"=>"KBRCK","Killester"=>"KLSTR","Lansdowne Rd"=>"LDWNE", "Malahide"=>"MHIDE","Seapoint"=>"SEAPT",  "Raheny"=>"RAHNY", "Salthill"=>"SHILL", "Shankill"=>"SKILL", "Sydney_Parade"=>"SIDNY", "Tara_Street"=>"TARA", "Sandymount"=>"SMONT", "Sandycove"=>"SCOVE",  "Portmarnock"=>"PMNCK",  "Killiney"=>"KILNY", "Howth_Jct"=>"HWTHJ", "Howth"=>"HOWTH",  "Glenageary"=>"GLGRY",  "Connolly"=>"CNLLY", "Bray"=>"BRAY", "Sutton"=>"SUTTN", "Pearse"=>"PERSE");


				$time_data = "http://www.irishrail.ie/realtime/publicinfo.asp?strLocation=".$url_places[$_GET["place"]];
				exec('curl "'.$time_data, $tmp_output); //So netsoc don't have remote url access allowed.

				$output = $tmp_output[0];	
				$parser = xml_parser_create();
				
				//xml_parse_into_struct($parser, $output, $results, $index);

				$title = 0; //Only display title fields, a hack but I don't know php :)
				function begin_cus($parser,$element_name, $element_attrs)
				{
						  global $title;
						  switch($element_name)
						  {
						   case "TITLE":
									  $title = 1;
									  break;
							default:
									  $title = 0;
						  }
				}

				function stop($parser,$element_name)
				{

						  //echo "</li><li>";
				}

				$currHour;
				$currMin;
				function char($parser, $data)
				{
						  global $title;
						  global $currHour;
						  global $currMin;
						  if($title == 1){
									 if(strstr($data, "is")){
												preg_match('/[0-9]+(?:\:[0-9]*)?/', $data, $matches);
												$tmp = explode(":",$matches[0]);
												$currHour = (int)$tmp[0];
												$currMin = (int)$tmp[1];	
									 }else{
												echo "\t\t\t\t<li>";
												echo $data;
												preg_match('/[0-9]+(?:\:[0-9]*)?/', $data, $matches);
												$tmp = explode(":", $matches[0]);
												if($currHour == (int)$tmp[0]){
													$time_rem = (int)$tmp[1] - $currMin;
												}else{
													$time_rem = (int)$tmp[1] +(60 - $currMin);
												}
												echo '<br/>'.$time_rem." mins";
				   	 					   echo "</li>\r";
									 }
						  }
				}

				xml_set_element_handler($parser,"begin_cus","stop");
				xml_set_character_data_handler($parser,"char");

				echo "<ul>\r";
				xml_parse($parser, $output);
				xml_parser_free($parser);
					
				echo "\t\t\t</ul>\r";
				//print_r($results[2]['value']);	

			?>
			</h2>
 		</div>
	</body>


</html>

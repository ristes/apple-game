Game.controller("PreloadController", function($scope, preloader, $timeout) {
	$scope.isLoading = true;
	$scope.isSuccessful = false;
	$scope.percentLoaded = 0;

	$scope.imageLocations = [
			("/public/images/home/igrac.png"),
			("/public/images/home/apple_icon_in_stock.png"),
			("/public/images/home/money_in_stock.png"),
			("/public/images/home/eco_points_in_stock.png"),
			("/public/images/backgrounds/rainy.png"),
			("/public/images/home/ramka.png"),
			("/public/images/home/prodavnica_strelka.png"),
			("/public/images/home/ploca.png"),
			("/public/images/home/drven_el.png"),
			("/public/images/game/items/eco-tractor.png"),
			("/public/images/game/items/grooves.png"),
//			("/public/images/game/operations/harvest.png"),
//			("/public/images/game/operations/plowing.png"),
//			("/public/images/game/operations/fertilizing.png"),
//			("/public/images/game/operations/digging.png"),
//			("/public/images/game/operations/irrigation.png"),
//			("/public/images/game/operations/spraying.png"),
			("/public/images/sky.png"),
			("/public/images/ograda.png"),
			("/public/images/backgrounds/sunny.png"),
			("/public/images/backgrounds/pozadinski-drva_snow.png"),
			("/public/images/backgrounds/stranicna-treva-desno.png"),
			("/public/images/backgrounds/side-terrain.png"),
			("/public/images/backgrounds/kukarka_snow.png"),
			("/public/images/backgrounds/field-terrain_snow.png"),
			("/public/images/backgrounds/field-terrain.png"),
			("/public/images/backgrounds/side-trees.png"),
			("/public/images/backgrounds/lake-1.png"),
			("/public/images/backgrounds/pozadinski-drva.png"),
			("/public/images/backgrounds/side-trees_snow.png"),
			("/public/images/backgrounds/kukarka.png"),
			("/public/images/backgrounds/cloudy.png"),
			("/public/images/backgrounds/rainy.png"),
			("/public/images/backgrounds/oblaci.png"),
			("/public/images/backgrounds/lake.png"),
			("/public/images/game/soil_types/2321.png"),
			("/public/images/game/soil_types/1421.png"),
			("/public/images/game/soil_types/2421.png"),
			("/public/images/game/soil_types/soil_snow.png"),
			("/public/images/game/soil_types/3132.png"),
			("/public/images/game/soil_types/2211.png"),
			("/public/images/game/soil_types/1231.png"),
			("/public/images/game/soil_types/2121.png"),
			("/public/images/game/soil_types/1332.png"),
			("/public/images/game/soil_types/1211.png"),
			("/public/images/game/soil_types/2111.png"),
			("/public/images/game/soil_types/1322.png"),
			("/public/images/game/soil_types/1122.png"),
			("/public/images/game/soil_types/1131.png"),
			("/public/images/game/soil_types/3122.png"),
			("/public/images/game/soil_types/1311.png"),
			("/public/images/game/soil_types/3111.png"),
			("/public/images/game/soil_types/2411.png"),
			("/public/images/game/soil_types/2431.png"),
			("/public/images/game/soil_types/2331.png"),
			("/public/images/game/soil_types/1331.png"),
			("/public/images/game/soil_types/3411.png"),
			("/public/images/game/soil_types/2311.png"),
			("/public/images/game/soil_types/2222.png"),
			("/public/images/game/soil_types/soil-snow.png"),
			("/public/images/game/soil_types/2432.png"),
			("/public/images/game/soil_types/1232.png"),
			("/public/images/game/soil_types/3332.png"),
			("/public/images/game/soil_types/2221.png"),
			("/public/images/game/soil_types/2412.png"),
			("/public/images/game/soil_types/1222.png"),
			("/public/images/game/soil_types/2312.png"),
			("/public/images/game/soil_types/1411.png"),
			("/public/images/game/soil_types/1112.png"),
			("/public/images/game/soil_types/3312.png"),
			("/public/images/game/soil_types/1422.png"),
			("/public/images/game/soil_types/3231.png"),
			("/public/images/game/soil_types/1212.png"),
			("/public/images/game/soil_types/3232.png"),
			("/public/images/game/soil_types/3112.png"),
			("/public/images/game/soil_types/3431.png"),
			("/public/images/game/soil_types/1321.png"),
			("/public/images/game/soil_types/1432.png"),
			("/public/images/game/soil_types/1312.png"),
			("/public/images/game/soil_types/3212.png"),
			("/public/images/game/soil_types/1412.png"),
			("/public/images/game/soil_types/2122.png"),
			("/public/images/game/soil_types/3311.png"),
			("/public/images/game/soil_types/3422.png"),
			("/public/images/game/soil_types/2132.png"),
			("/public/images/game/soil_types/2232.png"),
			("/public/images/game/soil_types/3432.png"),
			("/public/images/game/soil_types/2112.png"),
			("/public/images/game/soil_types/3211.png"),
			("/public/images/game/soil_types/1111.png"),
			("/public/images/game/soil_types/3321.png"),
			("/public/images/game/soil_types/2332.png"),
			("/public/images/game/soil_types/3221.png"),
			("/public/images/game/soil_types/2231.png"),
			("/public/images/game/soil_types/1132.png"),
			("/public/images/game/soil_types/1431.png"),
			("/public/images/game/soil_types/3412.png"),
			("/public/images/game/soil_types/3331.png"),
			("/public/images/game/soil_types/3421.png"),
			("/public/images/game/soil_types/2422.png"),
			("/public/images/game/soil_types/3222.png"),
			("/public/images/game/soil_types/3121.png"),
			("/public/images/game/soil_types/3131.png"),
			("/public/images/game/soil_types/1221.png"),
			("/public/images/game/soil_types/1121.png"),
			("/public/images/game/soil_types/2322.png"),
			("/public/images/game/soil_types/3322.png"),
			("/public/images/game/soil_types/2212.png"),
			("/public/images/game/soil_types/2131.png"),
			("/public/images/seasons/season-2.png"),
			("/public/images/seasons/season-4.png"),
			("/public/images/seasons/season-1.png"),
			("/public/images/seasons/season-3.png"),
			("/public/images/fridges/2.png"),
			("/public/images/fridges/0.png"),
			("/public/images/fridges/1.png"),
			("/public/images/mini-games/collect-game-bg.png"),
			("/public/images/mini-games/prune-tree.png"),
			("/public/images/fb_profile.png"),
			("/public/images/game/apple_tree/23.png"),
			("/public/images/game/apple_tree/32as.png"),
			("/public/images/game/apple_tree/31b.png"),
			("/public/images/game/apple_tree/34as.png"),
			("/public/images/game/apple_tree/33b.png"),
			("/public/images/game/apple_tree/22.png"),
			("/public/images/game/apple_tree/32b.png"),
			("/public/images/game/apple_tree/24.png"),
			("/public/images/game/apple_tree/32abr.png"),
			("/public/images/game/apple_tree/23b.png"),
			("/public/images/game/apple_tree/21.png"),
			("/public/images/game/apple_tree/34.png"),
			("/public/images/game/apple_tree/31.png"),
			("/public/images/game/apple_tree/32.png"),
			("/public/images/game/apple_tree/32abgr.png"),
			("/public/images/game/apple_tree/12.png"),
			("/public/images/game/apple_tree/13.png"),
			("/public/images/game/apple_tree/33.png"),
			("/public/images/game/apple_tree/11.png"),
			("/public/images/game/apple_tree/21b.png"),
			("/public/images/game/apple_tree/22a.png"),
			("/public/images/game/apple_tree/32abg.png"),
			("/public/images/game/apple_tree/34b.png"),
			("/public/images/game/apple_tree/14.png"),
			("/public/images/game/stores/soilanalyse.png"),
			("/public/images/game/stores/tractor.png"),
			("/public/images/game/stores/digging.png"),
			("/public/images/game/stores/other.png"),
			("/public/images/game/stores/irrigation.png"),
			("/public/images/game/stores/fertilizer.png"),
			("/public/images/game/stores/seedlings.png"),
			("/public/images/game/stores/sprays.png"),
			("/public/images/game/stores/terrain.png"),
			("/public/images/game/badges/fertilizer.png"),
			("/public/images/game/badges/trade.png"),
			("/public/images/game/badges/yield.png"),
			("/public/images/game/badges/irrigator.png"),
			("/public/images/game/badges/eco.png"),
			("/public/images/game/badges/harvester.png"),];
	
	$scope.menuitems = {
	'play':{
		description:"play"
	},
	
	'about': {
		description: "about"
	},
	'facts': {
		description: "facts"
	}};

	preloader.preloadImages($scope.imageLocations).then(
		function handleResolve(imageLocations) {
			// display welcome screen
			$scope.isLoading = false;
			$scope.isWelcoming = true;
			$scope.isIntroduction = false;
			
			$scope.hover = function(item) {
				$scope.current=item.description;
			}
			
			$scope.leave = function() {
				$scope.current="";
			}
			
			$scope.onPlay = function(flag) {
				$scope.current=flag;
				$scope.isIntroduction = false;
				$scope.isWelcoming = false;
				$scope.isSuccessful = true;
			}
			
			$scope.onHelp = function() {
				$scope.isIntroduction = false;
				$scope.isHelp = true;
			}
			
			$scope.onRules = function() {
				$scope.isIntroduction = false;
				$scope.isRules = true;
			}

			var hideWelcomeAnimation = function(){
				$timeout(function(){
					$scope.isIntroduction = true;
					$scope.isWelcoming = false;
					$scope.displayText= "Let's play!";
					$scope.$root.farmer.gameDate.weatherType.background_url = "/public/images/backgrounds/introduction.jpg";
				}, 2000)

				// start cloud leave animation
				$('.cloud-show-right').animate({
			    left: '-1000px'
			  }, 2000);

			  $('.cloud-show-left').animate({
			    left: '2000px'
			  }, 2000);
			}

			// hide welcome screen after 2 seconds and go into the game
			$timeout(hideWelcomeAnimation, 2000);
		},
		function handleReject(imageLocation) {
			$scope.isLoading = false;
			$scope.isSuccessful = false;
		},
		function handleNotify(event) {
			$scope.percentLoaded = event.percent;
		}

	);

});
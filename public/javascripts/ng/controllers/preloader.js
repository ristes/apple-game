Game
		.controller(
				"PreloadController",
				function($scope, preloader) {

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
							("/public/images/game/operations/harvest.png"),
							("/public/images/game/operations/plowing.png"),
							("/public/images/game/operations/fertilizing.png"),
							("/public/images/game/operations/digging.png"),
							("/public/images/game/operations/irrigation.png"),
							("/public/images/game/operations/spraying.png"),
							("/public/images/sky.png"),
							("/public/images/ograda.png"),
							("/public/images/deceases/crven-pajak.png"),
							("/public/images/deceases/jabolkov-crv.png"),
							("/public/images/deceases/pepelica.png"),
							("/public/images/deceases/predatorska-buba-mara-na-crven-pajak.png"),
							("/public/images/deceases/shteti-od-crven-pajak.png"),
							("/public/images/deceases/zashtita-od-jabolkov-crv.png"),
							("/public/images/diseases/Aphid.png"),
							("/public/images/diseases/Ascospore.png"),
							("/public/images/diseases/CodlingMoth.png"),
							("/public/images/diseases/crven-pajak.png"),
							("/public/images/diseases/FireBlight.png"),
							("/public/images/diseases/jabolkov-crv.png"),
							("/public/images/diseases/pepelica.png"),
							("/public/images/diseases/pepelica.png"),
							("/public/images/diseases/pepelica.png"),
							("/public/images/backgrounds/cloudy.png"),
							("/public/images/backgrounds/field-terrain.png"),
							("/public/images/backgrounds/kukarka.png"),
							("/public/images/backgrounds/lake.png"),
							("/public/images/backgrounds/oblaci.png"),
							("/public/images/backgrounds/pozadinski-drva.png"),
							("/public/images/backgrounds/rainy.png"),
							("/public/images/backgrounds/side-terrain.png"),
							("/public/images/backgrounds/side-trees.png"),
							("/public/images/backgrounds/stranicna-treva-desno.png"),
							("/public/images/backgrounds/sunny.png"),
							("/public/images/backgrounds/treva-stranicna.png"),
							("/public/images/backgrounds/nova scena/nebo.png"),
							("/public/images/backgrounds/nova scena/nezagadeno-ezero.png"),
							("/public/images/backgrounds/nova scena/pozadinski-drva.png"),
							("/public/images/backgrounds/nova scena/stranicni-drva.png"), ];

					preloader.preloadImages($scope.imageLocations).then(
							function handleResolve(imageLocations) {

								$scope.isLoading = false;
								$scope.isSuccessful = true;

								console.info("Preload Successful");

							}, function handleReject(imageLocation) {

								$scope.isLoading = false;
								$scope.isSuccessful = false;

								console.error("Image Failed", imageLocation);
								console.info("Preload Failure");

							}, function handleNotify(event) {

								$scope.percentLoaded = event.percent;

								console.info("Percent loaded:", event.percent);

							});

				});
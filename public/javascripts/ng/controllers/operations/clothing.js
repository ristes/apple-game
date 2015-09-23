Game
		.controller(
				'ClothingController',
				[
						'$scope',
						'Prunning',
						'State',
						'ExpertAdvice',
						'Modal',
						function($scope, Prunning, State, ExpertAdvice, Modal) {

							var self = this;

							var unreg = $scope.$root.$on('operation-prunning',
									function(_s, oper) {
										self.oper = oper;
										Prunning.checkPrune($scope.startPrune,
												$scope.stopPrune);

									});

							$scope.startPrune = function() {
								$scope.type = self.oper;
								$scope.gameActive = true;
								$scope.visible = true;
								$scope.$root.$emit("side-hide");
							}
							$scope.startGame = function() {
								$scope.modalInstance = Modal
										.start(
												"/public/templates/pruning-template.html",
												$scope);
							}
							$scope.stopPrune = function(message) {
								ExpertAdvice.setAdvice(message);
							}
							$scope.onHelp = function() {
								ExpertAdvice
										.setInfinteImportantAdvice("Pruning is multifunctional and dependent on the age of the fruit trees. Correct and timely pruning facilitates rapid, timely and proper formation of compact and productive crowns, which have the ability to utilize the maximum amount of sunlight. Improper pruning disturbs growth and reduces the yield and quality of fruits throughout the plant’s life span. Once errors are made in pruning,it is difficult to correct them.");
							}

							$scope.gameOver = function(gameResult) {
								$scope.gameActive = false;
								$scope.visible = false;
								ExpertAdvice
										.setAdvice("The prunning ended with "
												+ gameResult.correct
												+ " correct cutted branches. Great work! It’s time to rest and prepare for some hard work in the spring.");
								Prunning.prune(gameResult)
							}
							$scope.$root
									.$watch(
											'day.year_level',
											function(a) {

												$scope.year = State
														.getByField('farmer').year_level;

												if ($scope.year > 2) {
													$scope.year = 2;
												}
											})

							$scope.onResult = function(status) {
								$scope.hide();
							}

							$scope.$on("$destroy", function() {
								if (unreg) {
									unreg();
								}
							});

							$scope.hide = function() {
								$scope.visible = false;
							}

						} ]);

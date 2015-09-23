Game.controller('PlowingController', [
		'$scope',
		'Plowing',
	'fbShareActions',
	'ExpertAdvice',
		function($scope, Plowing,fbShareActions,ExpertAdvice) {
			
			$scope.holder = {};
			$scope.holder.deep = 20;
			
			

			var unreg = $scope.$root.$on('operation-plowing',
					function(_s, oper) {

						$scope.type = oper;

						$scope.plow = function() {
							Plowing.plowing($scope.holder.deep, function() {

								$scope.$root.$emit("show-animation-manager",{
									name:"plowing",
									type:"active-on-last-row"
								});
								$scope.visible = false;
//								$scope.$root.$emit('show-progress-global', {
//									title : 'progress.plowing',
//									duration : 10,
//									actionToShare:'Plowing',
//									backgroundSound: '/public/sounds/operations/plowing.mp3'
//								});
							});
						};

						$scope.onHelp = function() {
							ExpertAdvice
									.setInfinteImportantAdvice("Overgrowth of grass on plantations has a myriad of positive features: enhancing the soil qualities, boosting water absorption from precipitation, mitigation of erosion etc. Plantations are high-yielding if overgrown surfaces are properly maintained (for e.g. regularly mowed) and if there is increased application of fertilizers and water for irrigation. Growth of grass on the plantation has both advantages and disadvantages. It can enhance the soil quality, boost water absorption, and mitigate erosion. However, it can also act as a breeding ground for mice and increase the amount of water and nutrients required by the trees. Thus, it is important to carefully manage the height and spread of grass on the plantation; mowed grass can act as a fertilizer for the trees after 3 years of growth. In areas with larger amounts of precipitation, the grass can be allowed to grow higher. In areas of low precipitation it must be kept short.");
						}
						
						$scope.visible = true;
						$scope.$root.$emit('shop-hide');
						$scope.$root.$emit("side-hide");

						$scope.$on("$destroy", function() {
							if (unreg) {
								unreg();
							}
						});

						$scope.hide = function() {
							$scope.visible = false;
						}
						
						$scope.cfg = {
								range : "max",
								min : 0,
								max : 100,
								slide : function(event, ui) {
									$scope.$apply(function() {
										$scope.holder.deep = ui.value;
									});
								}
							};

						
					});
		} ]);
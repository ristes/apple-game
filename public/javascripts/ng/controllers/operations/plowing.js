Game.controller('PlowingController', [
		'$scope',
		'Plowing',
	'fbShareActions',
		function($scope, Plowing,fbShareActions) {
			
			$scope.holder = {};
			$scope.holder.deep = 20;
			
			

			var unreg = $scope.$root.$on('operation-plowing',
					function(_s, oper) {

						$scope.type = oper;

						$scope.plow = function() {
							Plowing.plowing($scope.holder.deep, function() {

								$scope.$root.$emit('show-progress-global', {
									title : 'progress.plowing',
									duration : 10
								});
								fbShareActions.shareAction('Plowing');
							});
						};

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
Game.controller('PlowingSceneController', [ '$scope', 'Planting',
		function($scope, Planting) {

			$scope.holder = {};
			$scope.holder.deep = 60;

			$scope.type = {
					name:'deep_plowing',
					url:"/public/images/game/operations/plowing.png"
			}
			$scope.plow = function() {
				Planting.firstDeepPlowing($scope.holder.deep, function() {

					$scope.$root.$emit('show-progress-global', {
						title : 'progress.plowing',
						duration : 10
					});
					

				});
			};

			$scope.visible = true;


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
				min : 30,
				max : 100,
				slide : function(event, ui) {
					$scope.$apply(function() {
						$scope.holder.deep = ui.value;
					});
				}
			};

		} ]);
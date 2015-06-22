Game.controller("HintEventController", [
		'$scope',
		'State',
		'Modal',
		'LearnState',
		function($scope, State, Modal,LearnState) {
			State.subscribe('hint-event', 'HintEventController',
					function(data) {
						$scope.message = data;
						$scope.modalInstance = Modal.start(
								'/public/templates/hint-dialog.html', $scope);
					});
			$scope.close = function() {
				$scope.modalInstance.close();
			}
			$scope.itemClick = function(a) {
				LearnState.save(a);
				$scope.modalInstance.close();
				
				$scope.$root.$emit('side-show');

			}
		} ])
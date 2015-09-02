GameDirectives.directive('customAudioNonsupervised', [
		'ngAudio',
		function(ngAudio) {
			return {
				transclude : true,
				restrict : 'E',
				scope : {
					filename: '@',
					startOnInit:'=',
					loop:'='
				},
				link: function($scope) {
					$scope.$on("$destroy", function() {
						$scope.audio.stop();
					});
				},
				templateUrl : '/public/templates/customAudio.html',
				controller: function($scope) {
					
					$scope.audio = ngAudio.load($scope.filename);
					if ($scope.loop===undefined) {
						$scope.audio.loop = true;
					} else {
						$scope.audio.loop = $scope.loop;
					}
					
					$scope.audio.volume = 0.5;
					//$scope.audio.play();
					if ($scope.startOnInit) {
						$scope.audio.play();
					}
					
				
					
					
				}
			};
		} ]);
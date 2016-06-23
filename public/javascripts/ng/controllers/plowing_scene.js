Game.controller('PlowingSceneController', ['$scope', 'Planting','Toaster',
    function($scope, Planting, Toaster) {
		
        $scope.holder = {};
        $scope.holder.deep = 60;

        $scope.type = {
            name: 'deep_plowing',
            url: "/public/images/game/operations/plowing.png"
        };

        $scope.plow = function() {
            Planting.firstDeepPlowing($scope.holder.deep, function() {
//                $scope.$root.$emit('show-progress-global', {
//                    title: 'progress.plowing',
//                    duration: 10
//                });
            	Toaster.success("Plowing is done! Great job!");
            });
        };

        $scope.visible = true;

        $scope.hide = function() {
            $scope.visible = false;
        }

        $scope.cfg = {
            range: "max",
            min: 30,
            max: 100,
            slide: function(event, ui) {
                $scope.$apply(function() {
                    $scope.holder.deep = ui.value;
                });
            }
        };
    }
]);

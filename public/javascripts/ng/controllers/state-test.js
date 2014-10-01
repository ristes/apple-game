Game.controller('StateTestController', ['$scope', '$day',
    function($scope, $day) {

      $scope.next = function() {
        $day.next();
        $scope.$root.context = $day;
      }

    }]);
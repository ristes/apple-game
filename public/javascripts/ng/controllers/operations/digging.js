Game.controller('DiggingController', ['$scope', 'Plowing',
    function($scope, Plowing) {

      var unreg = $scope.$root.$on('operation-digging', function(_s, oper) {

        Plowing.digging(function() {
          $scope.$root.$emit('show-progress-global', {
            title: 'progress.digging',
            duration: 5
          });
        });

      });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
      });

    }]);
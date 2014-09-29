Game.controller('PlowingController', ['$scope', 'Plowing',
    function($scope, Plowing) {

      var unreg = $scope.$root.$on('operation-plowing', function(_s, oper) {

        Plowing.plowing(function() {
          $scope.$root.$emit('show-progress-global', {
            title: 'progress.plowing',
            duration: 20
          });
        });
      });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
      });

    }]);
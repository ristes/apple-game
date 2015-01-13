Game.controller('ClothingController', ['$scope','Prunning','State', function($scope, Prunning, State) {

    var unreg = $scope.$root.$on('operation-pruning', function(_s, oper) {
        $scope.type = oper;
        $scope.gameActive = true;
        $scope.visible = true;
        $scope.$root.$emit("side-hide");
    });

    $scope.gameOver = function(gameResult) {
        $scope.gameActive = false;
        $scope.visible = false;
        Prunning.prune(gameResult)
    }
    $scope.$root.$watch('day.year_level', function(a) {
    
    	$scope.year = State.getByField('farmer').year_level;
    	
    	if ($scope.year>2) {
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

    $scope.hide = function(){
        $scope.visible = false;
    }

}]);

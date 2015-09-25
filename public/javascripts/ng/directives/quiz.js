GameDirectives
		.directive(
				'quizWindow',
				[
						'jQuery',
						'$window',
						'State',
						'Quiz',
						function($, $window, State, Quiz) {

							return {
								restrict : 'E',
								transclude : true,

								link : function($scope, element, attrs, ctrl,
										transclude, formCtrl) {

									$scope.quizvisible = false;
									$scope.questions = [];
									$scope.quiz_completed = false;

									$scope.$root.$watch("day.isNewSeason",
											function(n) {
												if (!n)
													return;
												if (n === true) {
													reload();
													Quiz.load();
													$scope.quiz_completed = false;	
												}

											});

									var onDataReceived = function(data) {
										if (data.status === false) {
											$scope.quiz_completed = true;
											$scope.apples_gained = data.t.apples_gained;
											$scope.eco_gained = data.t.eco_gained;
										} else {
											$scope.questions = data;
										}
									}
									State.subscribe('quiz', 'QuizController',
											onDataReceived);
									
									var reload = function() {
										
										$scope.score = 0;
										$scope.misses = 0;
										$scope.ordnum = 0;
										$scope.is_answered = false;
									}
									reload();
									$scope.is_answered = false;
									
									$scope.submit = function() {

									}
									$scope.next = function() {
										$scope.ordnum++;
										$scope.is_answered = false;
										if ($scope.ordnum === $scope.questions.length) {
											$scope.hide();
										}
									}

									$scope.answer = function(item) {
										$scope.is_answered = true;
										if (item.is_correct) {
											$scope.score++;
										} else {
											$scope.misses++;
										}
										item.qId = $scope.questions[$scope.ordnum].id;
										$scope.answers.push(item);
										// $scope.ordnum++;
										// if
										// ($scope.ordnum===$scope.questions.length)
										// {
										// $scope.hide();
										//        			
										// }
									}

									$scope.submit = function() {
										Quiz
												.submit(
														$scope.score,
														$scope.misses,
														function(data) {
															$scope.show();
															$scope.quiz_completed = true;
															$scope.apples_gained = data.t.apples_gained;
															$scope.eco_gained = data.t.eco_gained;
														});
									}

									$scope.question = {};
									$scope.ordnum = 0;
									$scope.data = [];
									$scope.answers = [];

									$scope.show = function() {
										$scope.$root.$emit("shop-hide");
										$scope.bgw = $window.innerWidth;
										$scope.bgh = $window.innerHeight;
										$scope.quizvisible = true;
									}
									$scope.hide = function() {
										$scope.bgw = 0;
										$scope.bgh = 0;
										$scope.quizvisible = false;
									}
									$scope.$root.$on("quiz-start", function() {
										$scope.show();
									});

									$scope.$root.$on("quiz-result", function() {
										$scope.quiz_completed = true;
										$scope.show();
									});

									var un = $scope.$root.$on("quiz-end",
											function() {
												reload();
												$scope.hide();
											});
								},
								templateUrl : '/public/templates/quiz-window.html'
							};

						} ]);
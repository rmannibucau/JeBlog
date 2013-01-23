// routing + services
angular.module('JeBlog', []).
  config(['$routeProvider', '$locationProvider', '$httpProvider', function($routeProvider, $locationProvider, $httpProvider) {
    $routeProvider
        .when('/login', { templateUrl: 'partials/login.html', controller: LoginController })
        .when('/logout', { templateUrl: 'partials/waiting.html', controller: LogoutController  })
        .when('/', { templateUrl: 'partials/posts.html',   controller: PostsController })
        .when('/posts/:postId', {templateUrl: 'partials/post.html', controller: PostController })
        .otherwise({ redirectTo: '/' });

        // $locationProvider.html5Mode(true);

        $httpProvider.defaults.transformRequest = function(data){
          return data != undefined ? $.param(data) : null;
        }
  }]
).factory('user', function() {  
    return {
        name: null
    };
});

// controllers

function LogStateController($scope, user) {
    $scope.name = user.name;
}

function LoginController($scope, $http, $location, user) {
    $scope.success = false;
    $scope.message = null;

    $scope.doLogin = function() {
        $http.post('api/user/login', { "username": $scope.username, "password": $scope.password },
                {
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
                })
                .success(function(data) {
                    user.name = $scope.username;
                    $location.path("/");
                })
                .error(function(data){
                    $scope.message = "Username or password incorrect.";
                });
    }
}

function LogoutController($scope, $http, $location, user) {
    $http.head('api/user/logout')
                .success(function(data) {
                    user.name = $scope.username;
                    $location.path("/");
                })
                .error(function(data){
                    user.name = $scope.username;
                    $location.path("/");
                });
}

function PostsController($scope, $http) {
  $scope.posts = $http.get('api/post/list')
                        .success(function(data) {
                            $scope.posts = data.post;
                        });
}

function PostController($scope, $http, $routeParams) {
  $scope.posts = $http.get('api/post/' + $routeParams.postId)
                        .success(function(data) {
                            $scope.posts = data.post;
                        });
}

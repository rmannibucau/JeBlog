// routing
angular.module('JeBlog', []).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/login', { templateUrl: 'partials/login.html', controller: LoginController })
        .when('/logout', { templateUrl: 'partials/waiting.html', controller: LogoutController  })
        .when('/', { templateUrl: 'partials/posts.html',   controller: PostsController })
        .when('/posts/:postId', {templateUrl: 'partials/post.html', controller: PostController })
        .otherwise({ redirectTo: '/' });
  }]
).config(function ($httpProvider) {
    $httpProvider.defaults.transformRequest = function(data){
        return data != undefined ? $.param(data) : null;
    }
});

// controllers

function LoginController($scope, $http, $location, $rootScope) {
    $scope.success = false;
    $scope.message = null;

    $scope.doLogin = function() {
        $http.post('api/user/login', { "username": $scope.username, "password": $scope.password },
                {
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
                })
                .success(function(data) {
                    $scope.message = "";
                    $rootScope.username = "TODO"; // TODO: read it from data
                    $location.path("/");
                })
                .error(function(data){
                    $scope.message = "Username or password incorrect.";
                });
    }
}

function LogoutController($scope, $http, $location, $rootScope) {
    $http.head('api/user/logout')
                .success(function(data) {
                    $rootScope.username = null;
                    $location.path("/");
                })
                .error(function(data){
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

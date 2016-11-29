var MainViewModel = function() {
    var self = this;
    var userService = new UserService();

    this.template = ko.observable({});
    var swapTemplate = function(template) {
        if (self.template().model && _.isFunction(self.template().model.dispose)) {
            self.template().model.dispose();
        }

        self.template(template);
    };
    var app = Sammy('#main', function() {
        this.before({except: []}, function() {
            if (!userService.isLogined()) {
                document.location = '/login';
            }
        });

        this.get('#/', function () {
            $.get('/dwellers/show/' + userService.getPersonId()).then(function (profileData) {
                var personId = userService.getPersonId();
                var viewModel = new ProfileViewModel(profileData, personId);
                swapTemplate({
                    name: 'profile-template',
                    model: viewModel
                })
            });
        });

        this.get('#/readings/submit', function () {
            $.get('/dwellers/show/' + userService.getPersonId()).then(function (profileData) {
                var viewModel = new MetersViewModel(profileData, true);
                swapTemplate({
                    name: 'meters-template',
                    model: viewModel
                });
            });
        });

        this.get('#/person/:personId', function () {
            $.get('/dwellers/show/' + this.params['personId']).then(function (profileData) {
                var viewModel = new ProfileViewModel(profileData);
                swapTemplate({
                    name: 'profile-template',
                    model: viewModel
                })
            });
        });

        this.get('#/meters/create', function () {
            var viewModel = new MetersCreateViewModel();
            swapTemplate({
                name: 'meters-create-template',
                model: viewModel
            });
        });

        this.get('#/flats/create', function () {
            var viewModel = new FlatsCreateViewModel();
            swapTemplate({
                name: 'flats-create-template',
                model: viewModel
            });
        });

        this.get('#/meters/statistics', function () {
            var userId = userService.getPersonId();
            $.get('/dwellers/show/' + userId).then(function (profileData) {
                var viewModel = new MetersStatisticsViewModel(profileData, userId);
                swapTemplate({
                    name: 'meters-statistic-template',
                    model: viewModel
                });
            });
        });

        this.get('#/history/:flatId', function () {
            $.get('/history/byFlatId/' + this.params['flatId']).then(function (historyGroups) {
                var viewModel = new MeasurementsHistoryViewModel(historyGroups);
                swapTemplate({
                    name: 'measurements-history-template',
                    model: viewModel
                })
            })
        });

        this.get('#/allDwellers', function () {
            $.when($.get('/dwellers/listAll'), $.get('/api/abnormal/consumers')).then(function(historyGroups, abnormalConsumers){
                var viewModel = new ListDwellersViewModel(historyGroups[0], abnormalConsumers[0]);
                swapTemplate({
                    name: 'list-dwellers-template',
                    model: viewModel
                })
            });
        });

        this.get('#/signUp', function () {
            var viewModel = new SignUpViewModel();
            swapTemplate({
                name: 'sign-up-template',
                model: viewModel
            });
        });

        this.get('#/signIn', function () {
            var viewModel = new SignInViewModel();
            swapTemplate({
                name: 'sign-in-template',
                model: viewModel
            });
        });

        this.get('#/serviceRequests/getInfo/:requestId', function () {
            var requestId = this.params['requestId'];
            var isEditable = false; //check if logined user is admin
            var personId = 1;
            $.when($.get('/api/request/' + requestId),
                $.get('/api/visited/' + requestId),
                $.get('/api/commented/' + requestId))
                .then(function (requestModel, visits, comments) {
                    var viewModel = new RequestViewModel(isEditable, requestModel[0], visits[0], comments[0], personId);
                    swapTemplate({
                        name: 'request-template',
                        model: viewModel
                    });
                })
        });

        this.get('#/serviceRequests/forFlat/:flatId', function () {
            var flatId = this.params['flatId'];
            $.get('/serviceRequests/byFlatId/' + flatId).then(function (requestsInfo) {
                var viewModel = new ServiceRequestsViewModel(requestsInfo, flatId);
                swapTemplate({
                    name: 'service-requests-template',
                    model: viewModel
                })
            })
        });

        this.get('#/debts', function () {
            $.get('/api/debt/' + userService.getPersonId()).then(function (debts) {
                var viewModel = new PersonalDebtsViewModel(debts);
                swapTemplate({
                    name: 'person-debts-template',
                    model: viewModel
                })
            })
        });

        this.get('#/serviceRequests/allActive', function () {
            $.get('/serviceRequests/allActive').then(function (requestsInfoList) {
                var viewModel = new ServiceRequestsAdminViewModel(requestsInfoList);
                swapTemplate({
                    name: 'service-requests-admin-template',
                    model: viewModel
                })
            })
        });

        this.get('admin#/debts/list', function () {
            $.get('/api/debts').then(function (debts) {
                var viewModel = new AllDebtsViewModel(debts);
                swapTemplate({
                    name: 'all-debts-template',
                    model: viewModel
                });
            });
        });

        this.get('#/serviceRequests/create/:flatId', function () {
            var flatId = this.params['flatId'];
            var viewModel = new CreateRequestViewModel(flatId);
            swapTemplate({
                name: 'request-create-template',
                model: viewModel
            });
        });

        this.get('#/dweller-lives-in-flat', function () {
            $.when($.get('/dwellers/listAll'), $.get('/api/flats')).then(function(dwellers, flats){
                var viewModel = new ConnectDwellerAndFlatViewModel(dwellers[0], flats[0]);
                swapTemplate({
                    name: 'connect-dweller-and-flat-template',
                    model: viewModel
                });
            });
        });
    });

    this.run = function (startUrl) {
        app.run(startUrl);
    };
    $(".nav a").on("click", function () {
        $(".nav").find(".active").removeClass("active");
        $(this).parent().addClass("active");
    });

    ko.applyBindings(self, $('#main')[0]);

    $('#signOut').click(function() {
        userService.logOut();
        location.reload();
    });
};
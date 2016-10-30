ko.bindingHandlers.datepicker = {
    init: function(element, valueAccessor, allBindingsAccessor) {
        //initialize datepicker with some optional options
        $(element).datepicker({
            format: 'yyyy-mm-dd'
        });

        //when a user changes the date, update the view model
        ko.utils.registerEventHandler(element, "changeDate", function(event) {
            var value = valueAccessor();
            if (ko.isObservable(value)) {
                value(event.date);
            }
        });
    },
    update: function(element, valueAccessor)   {
        var widget = $(element).data("datepicker");
        //when the view model is updated, update the widget
        if (widget) {
            widget.date = ko.utils.unwrapObservable(valueAccessor());
            widget.setValue();
        }
    }
};

$.fn.editInPlace = (method, options...) ->
    this.each ->
        methods =
            # public methods
            init: (options) ->
                console.log("dei init")
                valid = (e) =>
                    newValue = @input.val()
                    options.onChange.call(options.context, newValue)
                cancel = (e) =>
                    @el.show()
                    @input.hide()
                @el = $(this).dblclick(methods.edit)
                @input = $("<input type='text' />")
                    .insertBefore(@el)
                    .keyup (e) ->
                        switch(e.keyCode)
                            # Enter key
                            when 13 then $(this).blur()
                            # Escape key
                            when 27 then cancel(e)
                    .blur(valid)
                    .hide()
            edit: ->
                console.log(this.input)
                @input
                    .val(@el.text())
                    .show()
                    .focus()
                    .select()
                @el.hide()
            close: (newName) ->
                @input.hide()
                @el.text(newName).show()
        # jQuery approach: http://docs.jquery.com/Plugins/Authoring
        if (methods[method])
            return methods[ method ].apply(this, options)
        else if (typeof method == 'object')
            return methods.init.call(this, method)
        else
            $.error("Method " + method + " does not exist.")

class Folder extends Backbone.View

    events:
        "click    .toggle"          : "toggle"
        "click    .newProject"      : "newProject"
        "click    .removeFolder"    : "removeFolder"

    initialize: ->
        @id = this.$el.attr("data-group")
        @name = $(".folderName", @el).editInPlace
            context: this
            onChange: @renameFolder

    toggle: (e) ->
        e.preventDefault()
        @el.toggleClass("closed")
        false
    newProject: (e) ->
        jsRoutes.controllers.ProjectController.addProject().ajax
            context: this
            data:
                folder: this.$el.attr("data-group")
            success: (tpl) ->
                _list = $("ul",@el)
                _view = new Project
                    el: $(tpl).appendTo(_list)
                _view.$el.find(".name").editInPlace("edit")
            error: (err) ->
                $.error("Error: " + err)

    renameFolder: (newNameForFolder) ->
        #console.log(newNameForFolder)
        jsRoutes.controllers.FolderController.renameFolder(@id).ajax
            context: this
            data:
                name: newNameForFolder
            success: (data) ->
                @name.editInPlace("close", data)
            error: (err) ->
                $.error("Error: " + err)

    removeFolder: ->
        jsRoutes.controllers.FolderController.removeFolder(@id).ajax
            context: this
            success: ->
                this.$el.remove()
            error: (err) ->
                $.error("Error: "+err)




class Project extends Backbone.View

    events:
        "click    .delete"    : "deleteProject"

    initialize: ->
        @id = this.$el.attr("data-project")
        @name = $(".name", @el).editInPlace
            context: this
            onChange: @renameProject

    renameProject: (name) ->
        @loading(true)
        jsRoutes.controllers.ProjectController.rename(@id).ajax
            context: this
            data:
                name: name
            success: (data) ->
                @loading(false)
                @name.editInPlace("close", data)
            error: (err) ->
                @loading(false)
                $.error("Error: " + err)

    loading: (display) ->
        if (display)
            this.$el.children(".delete").hide()
            this.$el.children(".loader").show()
        else
            this.$el.children(".loader").hide()
            this.$el.children(".delete").show()

    deleteProject: (e) ->
        e.preventDefault()
        @loading(true)
        jsRoutes.controllers.ProjectController.delete(@id).ajax
            context: this
            success: ->
                this.$el.remove()
                @loading(false)
            error: (err) ->
                @loading(false)
                $.error("Error: " + err)
        false


class Drawer extends Backbone.View

    initialize: ->
        $("#newFolder").click @addNewFolder
        this.$el.children("li").each (i, folder) ->
            folder
            new Folder(el: $(folder))
            $("li", folder).each (i, project) ->
                new Project(el: $(project))

    addNewFolder: ->
        jsRoutes.controllers.FolderController.addFolder().ajax
            success: (data) ->
                _view = new Folder
                    el: $(data).appendTo("#projects")
                #console.log(_view.$el.find(".folderName"))
                #_view.$el.find(".folderName").editInPlace("edit")

class Task extends Backbone.View
    
    initialize: ->
        $("#addTaskButton").click @addTask
        console.log("inicializei uma task")

    addTask: ->
        @projectID = $(".projectGrouping").attr("data-project-id")
        console.log("projectID dessa task eh"+@projectID)
        jsRoutes.controllers.TaskController.addTask(@projectID).ajax
            context: this
            data:
                form: $("#formNewTask") 
            success: (data) ->
                console.log("coloquei a nova task na lista")
            error: (err) ->
                $.error("Error: " + err)



$ ->
    drawer = new Drawer(el: $("#projects"))
    tasks = new Task(el: $(".tasks"))
$.fn.editInPlace = (method, options...) ->
	this.each ->
		methods = 
			# public methods
			init: (options) ->
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
				@input
					.val(@el.text())
					.show()
					.focus()
					.select()
				@el.hide()
			close: (newName) ->
				@el.text(newName).show()
				@input.hide()
		# jQuery approach: http://docs.jquery.com/Plugins/Authoring
		if ( methods[method] )
			return methods[ method ].apply(this, options)
		else if (typeof method == 'object')
			return methods.init.call(this, method)
		else
			$.error("Method " + method + " does not exist.")

class Folder extends Backbone.View

	events:
		"click    .toggle"          : "toggle"
		"click    .newProject"      : "newProject"
	toggle: (e) ->
		e.preventDefault()
		@el.toggleClass("closed")
		false
	newProject: (e) ->
		#@el.removeClass("closed")
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

class Project extends Backbone.View

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


class Drawer extends Backbone.View

	initialize: ->
		this.$el.children("li").each (i, folder) ->
			new Folder(el: $(folder))
			$("li", folder).each (i, project) ->
				new Project(el: $(project))

$ ->
	drawer = new Drawer(el: $("#projects"))

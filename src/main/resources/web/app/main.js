var context = cubism.context()
              .step(1000) // Distance between data points in milliseconds
              .size(800); // Number of data points
              //.stop();

function stock(name) {
    return context.metric(function(start, stop, step, callback) {

        d3.json("/metrics/topic/" + name + "/?start=" + start.toISOString()
              + "&stop=" + stop.toISOString()
              + "&step=" + step, function(data) {
                if(!data) return callback(new Error("unable to load data"));
                callback(null, data.map(function(d) { return d.value; }));
        });

        /*
        var values = [];
        start = +start;
        stop = +stop;

        while(start < stop) {
            start += step;
            values.push(value = Math.floor(Math.random()*10)-5);
        }
		callback(null, values);
		*/
	}, name);
}

function draw_graph(stocks_list) {
	d3.select("body").append("div") // Add a vertical rule to the graph
          .attr("class", "rule")
          .call(context.rule());

    d3.select("#demo")                 // Select the div on which we want to act
      .selectAll(".axis")              // This is a standard D3 mechanism to bind data
      .data(["top"])                   // to a graph. In this case we're binding the axes
      .enter()                         // "top" and "bottom". Create two divs and give them
      .append("div")                   // the classes top axis and bottom axis respectively.
      .attr("class", function(d) {
      	return d + " axis";
      })
      .each(function(d) {              // For each of these axes, draw the axes with 4
      	d3.select(this)              // intervals and place them in their proper places.
      	  .call(context.axis()       // 4 ticks gives us an hourly axis.
      	  .ticks(4).orient(d));
      });

    d3.select("#demo")
      .selectAll(".horizon")
      .data(stocks_list.map(stock))
      .enter()
      .insert("div", ".bottom")        // Insert the graph in a div. Turn the div into
      .attr("class", "horizon")        // a horizon graph and format to 2 decimals places.
      .call(context.horizon()
      .format(d3.format("+,.2p")));

    context.on("focus", function(i) {
        d3.selectAll(".value").style("right",   // Make the rule coincide with the mouse
        	i == null ? null : context.size() - i + "px");
    });
}
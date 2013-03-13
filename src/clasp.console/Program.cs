using CommandLine;
using clojure.lang.Hosting;
using System.Xml;

namespace clasp.console
{
    class Program
    {
        class Options
        {
            [Option("c", "clj", DefaultValue = false)]
            public bool ClojureRepl { get; set; }
        } 

        static private readonly Options options = new Options();

        static void Main(string[] args)
        {
            var cmdParser = new CommandLineParser();
            cmdParser.ParseArguments(args, options);

            Clojure.AddNamespaceDirectoryMapping("clojurewm", @"src\clojurewm");

            //var replInit = "(use 'clojurewm.init)\n" + 
            //  "(in-ns 'clojurewm.init)\n" +
            //  "(main)";

            if (options.ClojureRepl)
                Clojure.Require("clojure.main").main("-r");
        }
    }
}

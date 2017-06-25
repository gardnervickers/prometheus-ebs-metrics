
/**
 * @constructor
 * @extends events.EventEmitter
 */
var process = function() {};

/**
 * @type {stream.ReadableStream}
 */
process.stdin;

/**
 * @type {stream.WritableStream}
 */
process.stdout;

/**
 * @type {stream.WritableStream}
 */
process.stderr;

/**
 * @type {Array.<string>}
 */
process.argv;

/**
 * @type {string}
 */
process.execPath;

/**
 */
process.abort = function() {};

/**
 * @param {string} directory
 */
process.chdir = function(directory) {};

/**
 * @return {string}
 * @nosideeffects
 */
process.cwd = function() {};

/**
 * @type {Object.<string,string>}
 */
process.env;

/**
 * @param {number=} code
 */
process.exit = function(code) {};

/**
 * @return {number}
 * @nosideeffects
 */
process.getgid = function() {};

/**
 * @param {number} id
 */
process.setgid = function(id) {};

/**
 * @return {number}
 * @nosideeffects
 */
process.getuid = function() {};

/**
 * @param {number} id
 */
process.setuid = function(id) {};

/**
 * @type {!string}
 */
process.version;

/**
 * @type {Object.<string,string>}
 */
process.versions;

/**
 * @type {Object.<string,*>}
 */
process.config;

/**
 * @param {number} pid
 * @param {string=} signal
 */
process.kill = function(pid, signal) {};

/**
 * @type {number}
 */
process.pid;

/**
 * @type {string}
 */
process.title;

/**
 * @type {string}
 */
process.arch;

/**
 * @type {string}
 */
process.platform;

/**
 * @return {Object.<string,number>}
 * @nosideeffects
 */
process.memoryUsage = function() {};

/**
 * @param {!function()} callback
 */
process.nextTick = function(callback) {};

/**
 * @param {number=} mask
 */
process.umask = function(mask) {};

/**
 * @return {number}
 * @nosideeffects
 */
process.uptime = function() {};

/**
 * @return {number}
 * @nosideeffects
 */
process.hrtime = function() {};


var http = {};

/**
 * @typedef {function(http.IncomingMessage, http.ServerResponse)}
 */
http.requestListener;

/**
 * @param {http.requestListener=} listener
 * @return {http.Server}
 */
http.createServer = function(listener) {};

/**
 * @param {http.requestListener=} listener
 * @constructor
 * @extends events.EventEmitter
 */
http.Server = function(listener) {};

/**
 * @param {(number|string)} portOrPath
 * @param {(string|Function)=} hostnameOrCallback
 * @param {Function=} callback
 */
http.Server.prototype.listen = function(portOrPath, hostnameOrCallback, callback) {};

/**
 */
http.Server.prototype.close = function() {};

/**
 * @constructor
 * @extends stream.Readable
 */
http.IncomingMessage = function() {};

/**
 * @type {?string}
 * */
http.IncomingMessage.prototype.method;

/**
 * @type {?string}
 */
http.IncomingMessage.prototype.url;

/**
 * @type {Object}
 * */
http.IncomingMessage.prototype.headers;

/**
 * @type {Object}
 * */
http.IncomingMessage.prototype.trailers;

/**
 * @type {string}
 */
http.IncomingMessage.prototype.httpVersion;

/**
 * @type {string}
 */
http.IncomingMessage.prototype.httpVersionMajor;

/**
 * @type {string}
 */
http.IncomingMessage.prototype.httpVersionMinor;

/**
 * @type {*}
 */
http.IncomingMessage.prototype.connection;

/**
 * @type {?number}
 */
http.IncomingMessage.prototype.statusCode;

/**
 * @type {net.Socket}
 */
http.IncomingMessage.prototype.socket;

/**
 * @param {number} msecs
 * @param {function()} callback
 */
http.IncomingMessage.prototype.setTimeout = function(msecs, callback) {};

/**
 * @constructor
 * @extends events.EventEmitter
 * @private
 */
http.ServerResponse = function() {};

/**
 */
http.ServerResponse.prototype.writeContinue = function() {};

/**
 * @param {number} statusCode
 * @param {*=} reasonPhrase
 * @param {*=} headers
 */
http.ServerResponse.prototype.writeHead = function(statusCode, reasonPhrase, headers) {};

/**
 * @type {number}
 */
http.ServerResponse.prototype.statusCode;

/**
 * @param {string} name
 * @param {string} value
 */
http.ServerResponse.prototype.setHeader = function(name, value) {};

/**
 * @param {string} name
 * @return {string|undefined} value
 */
http.ServerResponse.prototype.getHeader = function(name) {};

/**
 * @param {string} name
 */
http.ServerResponse.prototype.removeHeader = function(name) {};

/**
 * @param {string|Array|buffer.Buffer} chunk
 * @param {string=} encoding
 */
http.ServerResponse.prototype.write = function(chunk, encoding) {};

/**
 * @param {Object} headers
 */
http.ServerResponse.prototype.addTrailers = function(headers) {};

/**
 * @param {(string|Array|buffer.Buffer)=} data
 * @param {string=} encoding
 */
http.ServerResponse.prototype.end = function(data, encoding) {};

/**
 * @constructor
 * @extends events.EventEmitter
 * @private
 */
http.ClientRequest = function() {};

/**
 * @param {string|Array|buffer.Buffer} chunk
 * @param {string=} encoding
 */
http.ClientRequest.prototype.write = function(chunk, encoding) {};

/**
 * @param {(string|Array|buffer.Buffer)=} data
 * @param {string=} encoding
 */
http.ClientRequest.prototype.end = function(data, encoding) {};

/**
 */
http.ClientRequest.prototype.abort = function() {};

/**
 * @param {Object} options
 * @param {function(http.IncomingMessage)} callback
 * @return {http.ClientRequest}
 */
http.request = function(options, callback) {};

/**
 * @param {Object} options
 * @param {function(http.IncomingMessage)} callback
 * @return {http.ClientRequest}
 */
http.get = function(options, callback) {};

/**
 * @constructor
 * @extends events.EventEmitter
 */
http.Agent = function() {};

/**
 * @type {number}
 */
http.Agent.prototype.maxSockets;

/**
 * @type {number}
 */
http.Agent.prototype.sockets;

/**
 * @type {Array.<http.ClientRequest>}
 */
http.Agent.prototype.requests;

/**
 * @type {http.Agent}
 */
http.globalAgent;


diskspace.result.total
diskspace.result.used
diskspace.result.free
diskspace.result.status

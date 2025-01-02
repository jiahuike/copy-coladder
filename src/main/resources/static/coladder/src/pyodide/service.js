import {loadPyodide} from "pyodide";

export const service = async(input) => {
  let pyodide = await loadPyodide();
  return pyodide.runPythonAsync("1+1");
}
/*
service().then((result) => {
  console.log("Python says that 1+1 =", result);
});*/


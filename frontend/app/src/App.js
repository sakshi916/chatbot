import logo from './logo.svg';
import './App.css';
import axios from "axios";
import { useEffect, useRef, useState } from 'react';
function App() {
  const [res, setRes] = useState(null);
  const [req,setReq]=useState("HI")
  const inputRef = useRef(null);

  useEffect(()=>{
const button=document.getElementById("userInput")

button.addEventListener('keydown', function(event) {
  if (event.key === 'Enter') {
    submitReq()
    
    console.log('Enter key detected!');
    // Insert your custom logic here
  }
});
  },[])

  const fetchData = async (req2) => {
      try {
        
        // Making a GET request to a free dummy API
        const response = await axios.post('http://localhost:8080/query',req2);
        
        // Axios automatically parses JSON, data is available in response.data
        setRes(response.data); 
      } catch (err) {
        console.log(err)
      } 
    };

 
  const submitReq=()=>{
    fetchData(inputRef.current.value);
    inputRef.current.value = ''; 
  }
  return (
    <div className="App">
      <header className="App-header">
        <textarea style={{width:'30rem'}} rows={15} value={res} />
        <img src={logo} className="App-logo" alt="logo" />
        <input id="userInput" style={{width:'30rem',height:'2rem'}} ref={inputRef}/>
        <br></br>
        <input type='submit'  onClick={submitReq} />
      </header>
    </div>
  );
}

export default App;

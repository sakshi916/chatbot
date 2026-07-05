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
        /* //for streaming
        const source = new EventSource(`http://localhost:8080/api/chat/stream?message=${req2}`);
        source.onmessage = async (event) => {
            await new Promise(r => setTimeout(r, 30));
            setRes(prev => prev + " " + event.data); // append each token
        };
        source.onerror = () => source.close();
        */
        
        const response = await axios.post('http://localhost:8080/query',req2);
        setRes(response.data); 
      } catch (err) {
        console.log(err)
      } 
    };

 
  const submitReq=()=>{
    //setRes('')  //for streaming
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

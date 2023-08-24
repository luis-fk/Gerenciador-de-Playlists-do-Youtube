import React from 'react';
import './App.css';
import { FaArrowRight } from 'react-icons/fa';
import 'typeface-poppins';

function App() {
  return (

    <div className="App">
      <header>
        <img src={process.env.PUBLIC_URL + '/redPlay.png'} alt="Logo do Meu Site" />
      </header>

      <section>
        <section className="search-text">
          <h3 className="noMargin">
            <span className="white-text">BUSCAR</span> <span className="red-text">VÍDEOS</span>
          </h3>

          <form >
            <form className="searchVideos">
              <input type="text" name="pesquisa" placeholder="Digite sua pesquisa..." />
              <button type="submit"><FaArrowRight /></button>
            </form>
          </form>
        </section>
      </section>


      <section>
        <section className="search-text">
          <h3 className="noMargin">
            <span className="white-text">My</span> <span className="red-text">Playlists</span>
          </h3>

          <button className="round-button">GO!</button>

        </section>
      </section>



      <footer>
        <p>Todos os direitos reservados &copy; 2023</p>
      </footer>
    </div>
  );
}

export default App;

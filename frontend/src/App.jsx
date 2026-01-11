import { useEffect, useState } from "react";
import fileStore from "./store/fileStore";
import { downloadFile } from "./api/filesApi";

function App() {
  const { files, loadFiles, addFile, loading } = fileStore();
  const [file, setFile] = useState(null);

  useEffect(() => {
    loadFiles();
  }, []);

  const handleUpload = () => {
    if (file) addFile(file);
  };

  return (
    <div className="min-h-screen bg-zinc-900 text-zinc-100">
      <div className="max-w-3xl mx-auto px-6 py-10">
        {/* Header */}
        <h1 className="text-3xl font-bold mb-2 tracking-tight">
          My Files
        </h1>
        <p className="text-zinc-400 mb-8">
          Upload and manage your files
        </p>

        {/* Upload Section */}
        <div className="bg-zinc-800 border border-zinc-700 rounded-xl p-5 mb-8">
          <div className="flex items-center gap-4">
            <input
              type="file"
              onChange={(e) => setFile(e.target.files[0])}
              className="block w-full text-sm text-zinc-300
                file:mr-4 file:py-2 file:px-4
                file:rounded-lg file:border-0
                file:text-sm file:font-semibold
                file:bg-zinc-700 file:text-white
                hover:file:bg-zinc-600"
            />

            <button
              onClick={handleUpload}
              className="px-5 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-500 transition font-medium"
            >
              Upload
            </button>
          </div>
        </div>

        {/* File List */}
        {loading && (
          <div className="text-center text-zinc-400 py-6 animate-pulse">
            Loading files...
          </div>
        )}

        {!loading && files.length === 0 && (
          <div className="text-center text-zinc-400 py-10">
            No files uploaded yet.
          </div>
        )}

        <ul className="space-y-3">
          {files.map((file) => (
            <li
              key={file.id}
              className="flex items-center justify-between
                         bg-zinc-800 border border-zinc-700
                         rounded-lg px-4 py-3"
            >
              <div>
                <p className="font-medium">{file.name}</p>
                <p className="text-xs text-zinc-400">
                  {(file.size / 1024).toFixed(1)} KB
                </p>
              </div>

              <button
                onClick={() => downloadFile(file.id)}
                className="text-sm px-3 py-1.5 rounded-md
                           bg-zinc-700 hover:bg-zinc-600 transition"
              >
                Download
              </button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default App;

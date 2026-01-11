import { create } from "zustand";
import { fetchFiles, uploadFile } from "../api/filesApi";

const useFileStore = create((set) => ({
    files: [],
    loading: false,
    error: null,

    loadFiles: async () => {
        set({ loading: true });
        try {
            const files = await fetchFiles();
            set({ files, loading: false });
        } catch (err) {
            set({ error: "Failed to load files", loading: false });
        }
    },

    addFile: async (file) => {
        await uploadFile(file);
        const files = await fetchFiles();
        set({ files });
    }
}));

export default useFileStore;

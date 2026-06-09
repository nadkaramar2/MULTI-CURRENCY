import React, { useState, useEffect } from "react";

import Pagination from "@mui/material/Pagination";
import Stack from "@mui/material/Stack";
const MainPagination = (props) => {
  const [page, setPage] = useState(0);
  const [tCount, setTcount] = useState(10);
  const [startFrom, setStartFrom] = useState(0);
  const [lastFrom, setLastFrom] = useState(0);
  const [itemPage, setItemPage] = useState(10);
  const PagintionHander = (event, value) => {
    setPage(value);
    if (value >= page) {
      setStartFrom(lastFrom);
      setLastFrom(parseInt(lastFrom) + parseInt(itemPage));
    } else if (tCount > 0) {
      setStartFrom(startFrom - itemPage);
      setLastFrom(lastFrom - itemPage);
    }
  };
  const ItemPageHander = (e) => {
    setItemPage(e.target.value);
    setLastFrom(e.target.value);
    setStartFrom(1);
    setPage(1);
  };
  useEffect(() => {
    props.itemPageData(itemPage);
    props.tCountData(startFrom);
  }, [page]);

  return (
    <div className="bg-blue-200 px-4 flex items-center justify-between  sm:px-6">
      <div className="  sm:flex-1 sm:flex sm:items-center sm:justify-between">
        <div className="mx-2">
          <label htmlFor="" className="text-xs text-gray-700">
            Record Per Page
          </label>
          <select
            onChange={ItemPageHander}
            className="focus:ring-indigo-500 focus:border-indigo-500 mx-2 h-full py-0 px-4 border border-gray-900 bg-transparent text-gray-900 text-base rounded-md"
          >
            <option value="10">10</option>
            <option value="25">25</option>
            <option value="50">50</option>
            <option value="75">75</option>
            <option value="100">100</option>
          </select>
        </div>
        <div>
          <nav
            className="relative z-0 inline-flex rounded-md shadow-sm   items-center -space-x-px"
            aria-label="Pagination"
          >
            <div>
              <p className="text-sm text-gray-900  mx-3">
                Showing
                <span className="font-bold  mx-2 text-xs text-blue-700 ">
                  {startFrom}
                </span>
                <span>to</span>
                <span className="font-bold mx-2 text-xs text-blue-700">
                  {lastFrom}
                </span>
              </p>
            </div>
            <Stack spacing={2}>
              <Pagination
                count={tCount}
                page={page}
                onChange={PagintionHander}
                color="primary"
              />
            </Stack>
            {/* <div className=" space-x-2">
              <button
                onClick={prevPage}
                className="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
              >
                <span className="sr-only">Previous</span>
                <ChevronLeftIcon className="h-5 w-5" aria-hidden="true" />
              </button>

              <button
                onClick={nextpage}
                className="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
              >
                <span className="sr-only">Next</span>
                <ChevronRightIcon className="h-5 w-5" aria-hidden="true" />
              </button>
            </div> */}
          </nav>
        </div>
      </div>
    </div>
  );
};

export default MainPagination;

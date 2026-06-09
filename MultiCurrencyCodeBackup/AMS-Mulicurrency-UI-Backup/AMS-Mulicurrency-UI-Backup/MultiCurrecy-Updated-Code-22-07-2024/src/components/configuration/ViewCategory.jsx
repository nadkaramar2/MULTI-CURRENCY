import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintationReport from "../reports/AutoPagintationReport";
export default function ViewCategory() {
  const [alldata, setAlldata] = useState([]);
  const [searchKeyword, setSearchKeyword] = useState("");
    const [searchQuery, setSearchQuery] = useState("");
    const [itemPage, setItemPage] = useState(20);
    const [tcount, setTcount] = useState(1);
  let participantID = sessionStorage.getItem("Participantid");
 
 
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };
  useEffect(() => {
    viewCategory();
  }, []);
  const viewCategory = async () => {
    try {
      const response = await amsApi.post(
        `category_type/categoryTypedata`,
        {
          strParticipantId: participantID,
        }
      );

      if (response.data.code === "S0000") {
        setAlldata(response.data.category);

        
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

   useEffect(() => {
     if (searchQuery === "" || searchQuery === " ") {
       viewCategory();
     }
   }, [searchQuery]);

   const Search = async () => {
     try {
       const response = await amsApi.post(
         `category_type/categoryAcctSearch/pagination/20/1`,
         {
           keyword: searchQuery,
         }
       );
       if (response.data.code === "S0000") {
         setAlldata(response.data.viewCategorySearch);
         // handleShowSuccess(response.data.message);
       } else {
         handleShowError(response.data.message);
       }
     } catch (error) {
       // handleShowError(error.response.data.message);
     }
   };


  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                View Account Category
              </p>
            </div>
          </div>
        </div>

        <AutoPagintationReport
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          searchData={setSearchQuery}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={Search}
        />
        <div className="overflow-x-auto relative shadow-md ">
          {alldata.length > 0 && (
            <>
              <div className="table-wrp block max-h-[33rem] ">
                <table className="w-full text-sm text-left text-clack dark:text-blue-100">
                  <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                    <th scope="col" className="py-2.5 px-6 whitespace-nowrap ">
                      Type
                    </th>
                    <th scope="col" className="py-2.5 px-6  whitespace-nowrap ">
                      Description
                    </th>
                  </thead>

                  <tbody>
                    <>
                      {alldata.map(({ strType, strDescription }) => (
                        <tr
                          key={uuidv4()}
                          className="bg-blue-00 border-b hover:bg-blue-200
                    border-blue-400"
                        >
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {strType || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {
                                strDescription || "-"}
                            </div>
                          </td>
                        </tr>
                      ))}
                    </>
                  </tbody>
                </table>
              </div>
            </>
          )}
        </div>
      </div>
     

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* </div> */}
    </AppLayout>
  );
}
